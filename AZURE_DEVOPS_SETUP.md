# 🚀 Azure DevOps CI/CD Setup - Quick Start Guide

This guide will help you set up Azure DevOps CI/CD with Tophat integration in **3 simple steps**.

---

## 📋 What You'll Get

✅ **Automated builds** on every push/PR  
✅ **Tophat installation links** for easy testing  
✅ **AI code review** on pull requests  
✅ **Build artifacts** stored in Azure Storage  
✅ **PR comments** with installation links  

---

## 🎯 Quick Start (3 Steps)

### Step 1: Create Azure DevOps Account & Project (5 minutes)

1. Go to [https://dev.azure.com](https://dev.azure.com)
2. Sign in (or create Microsoft account)
3. Create organization: `eventful-dev` (or your choice)
4. Create project: `eventful-android-app`
5. ✅ Done!

### Step 2: Set Up Azure Storage (10 minutes)

1. Go to [Azure Portal](https://portal.azure.com)
2. Create **Storage Account**:
   - Name: `eventfulandroid` (must be unique)
   - Region: Same as Azure DevOps
   - Redundancy: LRS (cheapest)
3. Create **Container**: `android-builds`
4. Copy **Storage Account Key** (from Access Keys)
5. ✅ Done!

### Step 3: Configure Pipeline (15 minutes)

1. **Import code** to Azure DevOps:
   - Go to **Repos** → **Import**
   - URL: `https://github.com/nicolasmartino-dev/eventful-android-app.git`

2. **Create Variable Group**:
   - Go to **Pipelines** → **Library**
   - Create: `Eventful-Android-Secrets`
   - Add variables:
     - `GOOGLE_MAPS_API_KEY` (your key, mark as secret)
     - `STORAGE_ACCOUNT_NAME` (e.g., `eventfulandroid`)
     - `STORAGE_ACCOUNT_KEY` (from Step 2, mark as secret)

3. **Create Service Connection**:
   - Go to **Project Settings** → **Service connections**
   - Create **Azure Resource Manager** connection
   - Name: `Azure-Service-Connection`

4. **Create Pipeline**:
   - Go to **Pipelines** → **Create Pipeline**
   - Select **Azure Repos Git**
   - Choose **Existing YAML file**
   - Path: `/azure-pipelines.yml`
   - Run!

5. ✅ Done!

---

## 📱 Using Tophat Links

After a successful build:

1. Go to **Pipelines** → Your pipeline run
2. Check **"Generate Tophat Installation Link"** step
3. Copy the Tophat URL
4. Open **Tophat app** on your Mac
5. Paste URL → App installs automatically! 🎉

**Example Tophat URL:**
```
tophat://install/http?url=https://eventfulandroid.blob.core.windows.net/android-builds/main/123/app-debug.apk?<sas-token>
```

---

## 🤖 AI Code Review

### Enable AI Review on PRs

1. Create a **Pull Request** in Azure DevOps
2. Click **"Review"** or **"AI Review"** button
3. AI will analyze:
   - Code quality
   - Security issues
   - Best practices
   - Performance

### Alternative: GitHub Integration

If Azure DevOps AI isn't available:
1. Create **GitHub service connection**
2. Use GitHub for PR reviews
3. AI review works via GitHub Copilot

---

## 📂 Files Created

```
eventful-android-app/
├── azure-pipelines.yml          # Main CI/CD pipeline
├── scripts/
│   ├── generate-tophat-link.sh  # Standalone Tophat link generator
│   ├── azure-devops-setup.md    # Detailed setup guide
│   └── migrate-to-azure-devops.md # Migration guide
└── AZURE_DEVOPS_SETUP.md        # This file
```

---

## 🔧 Pipeline Features

### Build Stage
- ✅ Compiles Android APK
- ✅ Runs unit tests
- ✅ Publishes build artifacts
- ✅ Uploads APK to Azure Storage
- ✅ Generates Tophat link
- ✅ Posts PR comment with link

### Lint Stage
- ✅ Runs Android Lint
- ✅ Publishes lint results

---

## 🎯 What Happens on Each Push

1. **Code pushed** → Pipeline triggers
2. **Build starts** → Compiles APK
3. **APK uploaded** → Azure Storage
4. **Tophat link created** → Installation URL
5. **PR comment posted** → (if PR)
6. **Artifacts published** → Downloadable APK

---

## 📊 Pipeline Status

Add this badge to your README:

```markdown
[![Build Status](https://dev.azure.com/<org>/<project>/_apis/build/status/<pipeline-id>)](https://dev.azure.com/<org>/<project>/_build)
```

---

## 🐛 Troubleshooting

### Build Fails: "local.properties not found"
→ Check `GOOGLE_MAPS_API_KEY` in variable group

### Build Fails: "Storage upload failed"
→ Verify `STORAGE_ACCOUNT_NAME` and `STORAGE_ACCOUNT_KEY`

### Tophat Link Not Generated
→ Check Azure Storage upload step succeeded

### PR Comments Not Posting
→ Create GitHub service connection (optional)

---

## 📚 Detailed Guides

For more information, see:
- **Full Setup**: `scripts/azure-devops-setup.md`
- **Migration Guide**: `scripts/migrate-to-azure-devops.md`
- **Tophat Script**: `scripts/generate-tophat-link.sh`

---

## 🎉 Success Checklist

- [ ] Azure DevOps account created
- [ ] Project created
- [ ] Azure Storage configured
- [ ] Variable group with secrets
- [ ] Service connection created
- [ ] Code imported
- [ ] Pipeline runs successfully
- [ ] Tophat link generated
- [ ] App installs via Tophat
- [ ] PR created
- [ ] AI review works

---

## 💡 Pro Tips

1. **Use variable groups** for all secrets
2. **Enable branch policies** for main branch
3. **Set up notifications** for build failures
4. **Use build retention** to manage storage
5. **Add build badges** to README

---

## 🚀 Next Steps

1. ✅ Complete the 3 steps above
2. ✅ Test the pipeline
3. ✅ Create a test PR
4. ✅ Try AI code review
5. ✅ Install app via Tophat

---

**Need Help?** Check the detailed guides in `scripts/` directory or open an issue.

**Ready to start?** Follow the 3 steps above! 🎯


